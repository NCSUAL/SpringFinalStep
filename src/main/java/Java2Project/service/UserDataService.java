package Java2Project.service;

import Java2Project.domain.User;
import Java2Project.domain.UserData;
import Java2Project.dto.request.RequestRemoveUserData;
import Java2Project.dto.request.RequestUserData;
import Java2Project.repository.UserDataRepository;
import Java2Project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private UserRepository userRepository;

    public UserDataService(UserDataRepository userDataRepository, UserRepository userRepository){
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }

    public void saveUserData(User user, UserData userData){

        Optional<User> findUser = userRepository.findByUsername(user.getUsername());

        if(findUser.isPresent()){
            log.info("{}","사용자 이름이 존재함");

            //연관관계 재설정
            userData.setUser(findUser.get());
        }
        else{
            userRepository.save(user);
        }

        userDataRepository.save(userData);
    }

    @Transactional
    public UserData ModifyContent(RequestUserData requestUserData){
        int updateContent = userDataRepository.updateContentByIdAndUsername(
                requestUserData.content(),
                requestUserData.userData_id(),
                requestUserData.username());


        log.info("{}", (updateContent ==0? "업데이트가 안됨": "정상적으로 됨") );


        return userDataRepository.findById(requestUserData.userData_id()).get();
    }

    @Transactional
    public List<UserData> removeContent(RequestRemoveUserData requestRemoveUserData){
        userDataRepository.deleteById(requestRemoveUserData.userData_id());

        return userDataRepository.findByUsername(requestRemoveUserData.username());
    }
}
