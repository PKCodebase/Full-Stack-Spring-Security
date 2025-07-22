

import { ToastContainer } from 'react-toastify'
import './App.css'
import { Route,Routes } from 'react-router-dom'
import EmailVerify from './pages/EmailVerify'
import Home from './pages/Home'
import ResetPassword from './pages/ResetPassword'
import Login from './pages/Login'

const App = ()=>{
    return(
      <div>
        <ToastContainer/>
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login/>}/>
            <Route path="/email-verify" element={<EmailVerify />} />
            <Route path="/reset-password" element={<ResetPassword />} />
        </Routes>
      </div>
    )
}
 
export default App
