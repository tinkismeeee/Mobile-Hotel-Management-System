# Mobile-Hotel-Management-System
Hệ thống đặt phòng khách sạn trên Android sử dụng ngôn ngữ lập trình Kotlin 

---

## Giới thiệu

Mobile-Hotel-Management-System là ứng dụng hỗ trợ khách hàng đặt phòng, thanh toán và quản lý dịch vụ khách sạn một cách tiện lợi ngay trên điện thoại. Hệ thống cung cấp hai vai trò chính: **Admin** để quản lý toàn bộ hoạt động khách sạn, và **Customer** để trải nghiệm dịch vụ. Phần backend được xây dựng bằng **NodeJS** với framework **ExpressJS**, đảm bảo tốc độ xử lý nhanh, khả năng mở rộng và quản lý dữ liệu mượt mà.

## 💻 Ứng dụng gồm 3 thành phần chính: 
- App: [Android App](https://github.com/tinkismeeee/Mobile-Hotel-Management-System)
- Frontend: [Website](https://github.com/thaikhang113/hotell/tree/master/frontend)
- Backend: [Server](https://github.com/thaikhang113/hotell/tree/master/backend)

## ⚙️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|------------|-----------|
| Mobile App | ![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white) |
| Backend    | ![NodeJS](https://img.shields.io/badge/Node.js-43853D?style=for-the-badge&logo=node.js&logoColor=white) ![ExpressJS](https://img.shields.io/badge/Express.js-000000?style=for-the-badge&logo=express&logoColor=white) |
| Frontend   | ![Handlebars](https://img.shields.io/badge/Handlebars.js-f0772b?style=for-the-badge&logo=handlebarsdotjs&logoColor=white) ![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white) |
| Database   | ![SQL](https://img.shields.io/badge/SQL-4479A1?style=for-the-badge&logo=postgresql&logoColor=white) |

## ⚠️ Lưu ý

- Do backend được chạy trên VPS có thời hạn nên ứng dụng có thể **không hoạt động bình thường** hoặc **ngừng hoạt động sau ngày 18/12/2025**.  
- Nếu muốn tiếp tục sử dụng sau thời gian này, cần triển khai lại backend trên VPS mới hoặc máy chủ khác.  
- Khi thay đổi VPS, hãy cập nhật lại địa chỉ IP và cấu hình trong file `API` để ứng dụng có thể kết nối đến địa chỉ của máy chủ mới.

## ✨ Các chức năng cơ bản

- Đăng ký tài khoản (Sign up)  
- Đăng nhập (Sign in)  
- Quên mật khẩu (Forgot password)  
- Xác thực email bằng mã OTP
- Trang chủ với thông tin người dùng, filter loại phòng, danh sách phòng  
- Filter loại phòng (Standard, Family, Deluxe, Suite)  
- Đặt phòng với dịch vụ đi kèm và chọn ngày nhận/trả  
- Thông báo đẩy khi đặt phòng thành công + lưu lịch sử thông báo  
- Lịch sử đặt phòng (My Booking)  
- Thanh toán phòng đã đặt trước (VAT, mã giảm giá)  
- Thanh toán bằng mã QR VietQR
- Chatbot AI (Gemini) gợi ý phòng theo điều kiện  
- Giới thiệu khách sạn (About) bằng WebView  
- Profile (trang thông tin người dùng)  
- Đăng nhập bằng Facebook 
- Quản lý khách sạn (Admin) 
  - Quản lý nhân viên  
  - Quản lý khách hàng  
  - Quản lý dịch vụ  
  - Quản lý loại phòng  
  - Quản lý doanh thu  

## 👥 Thành viên nhóm

- [**Nguyễn Hữu Tính**](https://github.com/tinkismeeee) 

- [Phan Ngọc Huy](https://github.com/Huytc147)

- [Cao Thái Khang](https://github.com/thaikhang113)

- [Vũ Huy Đức](https://github.com/HuyDuck2005)

- [Đỗ Anh Kiệt](https://github.com/BirdBB27)
