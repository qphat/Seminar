package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.response.DelegateResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.response.StatusResponse;
import com.seminar.seminar.service.DelegateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DelegateServiceImp implements DelegateService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 2.1 Lấy danh sách đại biểu (user với role ROLE_DELEGATE)
     * @return List<User>
     */
    public List<User> getAllDelegates() {
        return userRepository.findByRole(Role.DELEGATE);
    }

    /**
     * 2.2 Cập nhật thông tin đại biểu
     *
     * @param id   ID của user (đại biểu)
     * @param user Đối tượng chứa thông tin mới
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy hoặc không phải ROLE_DELEGATE
     */
    public StatusResponse updateDelegate(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Delegate not found with id: " + id);
        }

        User delegateToUpdate = existingUser.get();
        if (delegateToUpdate.getRole() != Role.DELEGATE) {
            throw new IllegalArgumentException("User with id " + id + " is not a delegate");
        }

        delegateToUpdate.setFullName(user.getFullName() != null ? user.getFullName() : delegateToUpdate.getFullName());
        delegateToUpdate.setPhone(user.getPhone() != null ? user.getPhone() : delegateToUpdate.getPhone());
        userRepository.save(delegateToUpdate);

        return new StatusResponse("success", "Delegate updated successfully");
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
     *
     * @param id ID của user (đại biểu)
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy hoặc không phải ROLE_DELEGATE
     */
    public DeleteResponse deleteDelegate(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User delegateToDelete = existingUser.get();
            if (delegateToDelete.getRole() != Role.DELEGATE) {
                throw new IllegalArgumentException("User with id " + id + " is not a delegate");
            }
            userRepository.deleteById(id);
            return new DeleteResponse("success", "Delegate deleted successfully");
        } else {
            throw new IllegalArgumentException("Delegate not found with id: " + id);
        }
    }

    /**
     * 2.4 Thêm đại biểu mới
     * @param user Đối tượng chứa thông tin đại biểu mới
     * @return DelegateResponse chứa thông tin đại biểu vừa tạo
     * @throws IllegalArgumentException nếu email đã tồn tại
     */
    public DelegateResponse createDelegate(User user) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email " + user.getEmail() + " is already in use");
        }

        // Tạo đại biểu mới
        User newDelegate = new User();
        newDelegate.setFullName(user.getFullName());
        newDelegate.setEmail(user.getEmail());
        newDelegate.setPhone(user.getPhone());
        newDelegate.setRole(Role.DELEGATE);

        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode("123456");
        newDelegate.setPassword(encodedPassword);

        // Lưu vào cơ sở dữ liệu
        User savedDelegate = userRepository.save(newDelegate);

        // Trả về DelegateResponse
        return new DelegateResponse(savedDelegate);
    }
}
