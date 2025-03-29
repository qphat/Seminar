# Quản Lý Đăng Ký Hội Thảo

Đây là một ứng dụng Java để quản lý việc đăng ký tham gia hội thảo. Ứng dụng cho phép lấy danh sách đại biểu đã đăng ký, danh sách hội thảo của một đại biểu, chi tiết tất cả các đăng ký, và cập nhật trạng thái đăng ký.

## Tính năng

- **Lấy danh sách đại biểu đã đăng ký hội thảo**: Trả về thông tin đại biểu (ID, họ tên, email) cho một hội thảo cụ thể.
- **Lấy danh sách hội thảo của đại biểu**: Hiển thị thông tin các hội thảo mà một đại biểu đã đăng ký cùng trạng thái.
- **Lấy tất cả đăng ký**: Trả về chi tiết toàn bộ các đăng ký trong hệ thống.
- **Cập nhật trạng thái đăng ký**: Cho phép thay đổi trạng thái đăng ký (PENDING, CONFIRMED, CANCELED).

## Công nghệ sử dụng

- **Ngôn ngữ lập trình**: Java, TypeScript
- **Framework**: Spring (Spring, Angular)
- **Cơ sở dữ liệu**: MySQL
- **Thư viện**: Stream API, Collectors

## Cài đặt

### Yêu cầu
- JDK 11 hoặc cao hơn
- Maven (để quản lý dependency)
- Cơ sở dữ liệu (cấu hình trong application.properties hoặc application.yml)

### Hướng dẫn cài đặt

1. **Clone repository**:
   ```bash
   git clone <https://github.com/>
   cd <ten-thu-muc>
   ```

2. **Cấu hình cơ sở dữ liệu**:
    - Tạo một cơ sở dữ liệu (ví dụ: conference_db).
    - Cập nhật thông tin kết nối trong file cấu hình:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/conference_db
      spring.datasource.username=root
      spring.datasource.password=your_password
      spring.jpa.hibernate.ddl-auto=update
      ```

3. **Cài đặt dependency**:
   ```bash
   mvn clean install
   ```

4. **Chạy ứng dụng**:
   ```bash
   mvn spring-boot:run
   ```

## Sử dụng

### Các phương thức chính

#### `getRegisteredDelegates(Long conferenceId)`
- **Mô tả**: Lấy danh sách đại biểu đã đăng ký cho một hội thảo.
- **Tham số**: `conferenceId` - ID của hội thảo.
- **Trả về**: Danh sách `RegistrationResponse` (delegateId, fullName, email, status).
- **Ngoại lệ**: `IllegalArgumentException` nếu hội thảo không tồn tại.

#### `getConferencesByDelegate(Long delegateId)`
- **Mô tả**: Lấy danh sách hội thảo mà một đại biểu đã đăng ký.
- **Tham số**: `delegateId` - ID của đại biểu.
- **Trả về**: Danh sách `ListRegistrationsResponse` (thông tin hội thảo và trạng thái).

#### `getAllRegistrations()`
- **Mô tả**: Lấy toàn bộ thông tin đăng ký trong hệ thống.
- **Trả về**: Danh sách `RegistrationDetailsResponse` (chi tiết đăng ký).

#### `updateRegistrationStatus(Long delegateId, Long conferenceId, String newStatus)`
- **Mô tả**: Cập nhật trạng thái đăng ký của đại biểu cho một hội thảo.
- **Tham số**:
    - `delegateId` - ID của đại biểu.
    - `conferenceId` - ID của hội thảo.
    - `newStatus` - Trạng thái mới (PENDING, CONFIRMED, CANCELED).
- **Trả về**: `UpdateStatusResponse` (success/error và thông báo).

### Ví dụ sử dụng

```java
// Lấy danh sách đại biểu của hội thảo có ID = 1
List<RegistrationResponse> delegates = conferenceService.getRegisteredDelegates(1L);
delegates.forEach(System.out::println);

// Cập nhật trạng thái đăng ký
UpdateStatusResponse response = conferenceService.updateRegistrationStatus(1L, 1L, "CONFIRMED");
System.out.println(response.getMessage());
```

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/
│   │   └── com/example/conference/
│   │       ├── model/              # Các lớp entity (Conference, Registration, User)
│   │       ├── repository/         # Interface repository (ConferenceRepository, RegistrationRepository)
│   │       ├── service/            # Logic nghiệp vụ (ConferenceService)
│   │       └── response/           # Các lớp DTO (RegistrationResponse, ListRegistrationsResponse, ...)
│   └── resources/
│       └── application.properties  # Cấu hình ứng dụng
```
