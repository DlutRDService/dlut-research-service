import React, { useState, useEffect } from 'react';
import './Modal.css';

const Modal = ({ isOpen, onClose, onVerify, username, password }) => {
  const [code, setCode] = useState('');
  const [captchaUrl, setCaptchaUrl] = useState('');

  useEffect(() => {
    if (isOpen) {
      fetchCaptcha();
    }
  }, [isOpen, username, password]);
  const formData = new URLSearchParams();

   const fetchCaptcha = async () => {
    if(formData.get('username')?.length>=1){
      return;
    }
    formData.append('username', username);
    formData.append('password', password);
    
    try {
      const response = await fetch('http://localhost:8080/login/sign-in/getCaptcha', {
        method: 'POST',
        body: formData,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
      });
      const data = await response.blob();
      setCaptchaUrl(URL.createObjectURL(data));
    } catch (error) {
      console.error('获取验证码失败:', error);
    }
  };

  const verifyCaptchaCode = async (codeToVerify) => {
    formData.append('code', codeToVerify); // 假设后端接收名为'code'的参数

    try {
      const response = await fetch('http://localhost:8080/login/sign-in/verifyCaptcha', {
        method: 'POST',
        body: formData,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
      });
      const result = await response.json(); // 假设后端返回一个JSON对象
      if (result.success) {
        // 校验成功
        onVerify(true); // 你可能需要传递一些数据给onVerify函数
        alert('验证码校验成功！');
        onClose(); // 关闭模态窗口
      } else {
        // 校验失败
        onVerify(false); // 传递失败状态
        alert('验证码校验失败，请重试！');
        fetchCaptcha(); // 可以选择是否在这里重新获取验证码
      }
    } catch (error) {
      console.error('校验验证码失败:', error);
      alert('校验验证码时发生错误，请重试！');
    }
  };

  const handleCaptchaClick = () => {
    fetchCaptcha();
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    verifyCaptchaCode(code); // 使用新函数来校验验证码
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>验证码校验</h2>
        <div className="captcha-container"> {/* 新增的包裹容器 */}
          {captchaUrl && (
            <img
              src={captchaUrl}
              alt="验证码"
              className="captcha-image"
              onClick={handleCaptchaClick}
            />
          )}
          <input
            type="text"
            value={code}
            onChange={(e) => setCode(e.target.value)}
            placeholder="请输入验证码"
            className="captcha-input"
          />
        </div> {/* 结束包裹容器 */}
        <form onSubmit={handleSubmit}>
          <div className="modal-actions">
            <button type="submit" className="verify-button">登录</button>
            <button onClick={onClose} className="close-button">关闭</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Modal;
