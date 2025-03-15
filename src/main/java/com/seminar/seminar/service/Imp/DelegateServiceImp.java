package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.response.DelegateResponse;
import com.seminar.seminar.service.DelegateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DelegateServiceImp implements DelegateService {


    private final UserRepository userRepository;

    /**
     * 2.1 Lấy danh sách đại biểu (user với role ROLE_DELEGATE)
     * @return List<User>
     */
    public List<User> getAllDelegates() {
        return userRepository.findByRole(Role.DELEGATE);
    }

    /**
     * 2.2 Cập nhật thông tin đại biểu
     * @param id ID của user (đại biểu)
     * @param user Đối tượng chứa thông tin mới
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy hoặc không phải ROLE_DELEGATE
     */
    public String updateDelegate(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User delegateToUpdate = existingUser.get();
            if (delegateToUpdate.getRole() != Role.DELEGATE) {
                throw new IllegalArgumentException("User with id " + id + " is not a delegate");
            }
            delegateToUpdate.setFullName(user.getFullName() != null ? user.getFullName() : delegateToUpdate.getFullName());
            userRepository.save(delegateToUpdate);
            return "Delegate updated successfully";
        } else {
            throw new IllegalArgumentException("Delegate not found with id: " + id);
        }
    }

    public DelegateResponse getDelegateById(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return new DelegateResponse(user);
        } else {
            throw new IllegalArgumentException("Delegate not found with id: " + id);
        }
    }

    /**
     * 2.3 Xóa đại biểu
     * @param id ID của user (đại biểu)
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy hoặc không phải ROLE_DELEGATE
     */
    public String deleteDelegate(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User delegateToUpdate = existingUser.get();
            if (delegateToUpdate.getRole() != Role.DELEGATE) {
                throw new IllegalArgumentException("User with id " + id + " is not a delegate");
            }
            userRepository.deleteById(id);
            return "Delegate deleted successfully";
        } else {
            throw new IllegalArgumentException("Delegate not found with id: " + id);
        }
    }
}
