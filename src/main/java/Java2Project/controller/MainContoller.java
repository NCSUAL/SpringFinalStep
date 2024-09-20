package Java2Project.controller;

import Java2Project.domain.User;
import Java2Project.domain.UserData;
import Java2Project.dto.request.RequestRemoveUserData;
import Java2Project.dto.request.RequestUser;
import Java2Project.dto.request.RequestUserData;
import Java2Project.dto.response.ResponseUserData;
import Java2Project.dto.response.ResponseUser;
import Java2Project.service.UserDataService;
import Java2Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class MainContoller{

    @Autowired
    private UserService userService;

    @Autowired
    private UserDataService userDataService;

    //전체 유저 조회
    @GetMapping("/user")
    public List<ResponseUser> getUsers(){
        List<ResponseUser> responseUsers = new ArrayList<>();
        List<User> users = userService.findAll();

        for(User user: users){
            responseUsers.add(ResponseUser.of(user.getId(),user.getUsername(),user.getCreatedAt()));
        }

        return responseUsers;
    }

    //특정 유저 조회
    @GetMapping("/user/{id}")
    public ResponseUser getUser(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseUser.of(user.getId(), user.getUsername(), user.getCreatedAt());
    }

    //특정 유저 이름 수정
    @PostMapping("/user")
    public ResponseUser postUser(@RequestBody RequestUser requestUser){
        User user = userService.ModifyUsername(requestUser);
        return ResponseUser.of(user.getId(),user.getUsername(),user.getCreatedAt());
    }

    //특정 유저 삭제
    @DeleteMapping("/user/{id}")
    public List<ResponseUser> deleteUser(@PathVariable Long id){
        List<User> users = userService.deleteById(id);
        List<ResponseUser> responseUsers = new ArrayList<>();

        for(User user: users){
            responseUsers.add(
                    ResponseUser.of(user.getId(), user.getUsername(), user.getCreatedAt())
            );
        }

        return responseUsers;
    }

    //------------------------------------------------------------------------------------------
    //특정 사용자 이름으로 등록된 내용들 조회
    @GetMapping("/content/{username}")
    public List<ResponseUserData> getContents(@PathVariable String username){
        List<UserData> userdatas = userService.findContents(username);
        List<ResponseUserData> responseUserData = new ArrayList<>();

        for(UserData userData: userdatas){
            responseUserData.add(ResponseUserData.of(username,userData.getId(),userData.getContent()));
        }

        return responseUserData;
    }

    //특정 사용자 이름으로 등록된 내용 수정
    @PatchMapping("/content")
    public ResponseUserData postContent(@RequestBody RequestUserData requestUserData){
        UserData userData = userDataService.ModifyContent(requestUserData);
        return ResponseUserData.of(userData.getUser().getUsername(),userData.getId(),userData.getContent());
    }

    //특정 사용자 이름으로 등록된 내용 삭제
    @DeleteMapping("/content")
    public List<ResponseUserData> deleteContent(@RequestBody RequestRemoveUserData requestRemoveUserData){
        List<UserData> userDatas = userDataService.removeContent(requestRemoveUserData);
        List<ResponseUserData> responseUserDatas = new ArrayList<>();

        for(UserData userData: userDatas){
            responseUserDatas.add(
                    ResponseUserData.of(userData.getUser().getUsername(),userData.getId(),userData.getContent())
            );
        }
        return responseUserDatas;
    }
}
