import React, { useState } from 'react';
import Modal from '../components/modal/Modal'; // Assuming Modal component is in the same directory

const Home = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [username, setUsername] = useState(''); // 添加用户名状态
  const [password, setPassword] = useState(''); // 添加密码状态

  const handleLoginClick = () => {
    // 在这里可能需要验证用户名和密码是否已经输入
    setIsModalOpen(true); // 打开模态窗口
  };

  const handleCloseModal = () => {
    setIsModalOpen(false); // 关闭模态窗口
  };

  const handleVerifyCode = (code) => {
    console.log('Entered code:', code);
    // 在这里添加验证码校验逻辑
    setIsModalOpen(false); // 假设验证码正确，关闭模态窗口
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>科学研究服务平台</h1>
      </header>
      <main className="App-main">
        <div className="login-form">
          <div className="form-container">
            <div className="form-group">
              <label htmlFor="username">用户名</label>
              <input 
                type="text" 
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)} // 更新用户名状态
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">密码</label>
              <input 
                type="password" 
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)} // 更新密码状态
              />
            </div>
            <button type="button" onClick={handleLoginClick}>登录</button>
          </div>
        </div>
        <Modal 
          isOpen={isModalOpen}
          onClose={handleCloseModal}
          onVerify={handleVerifyCode}
          username={username} // 传递用户名
          password={password} // 传递密码
        />
      </main>
    </div>
  );
};

export default Home;
