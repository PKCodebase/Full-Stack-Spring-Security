import { assets } from "../assets/assets";
import { Link } from "react-router-dom";
import { useState } from "react";
const Login = () =>{
    const[isCreateAccount,setIsCreateAccount] = useState(true);
    return(
        <div className="position-relative min-vh-100 d-flex justify-content-center align-items-center"
        style={{background: "linear-gradient(80deg, #f4d6f0, #c4bdbdff, #f4d8ff)",border:"none"}}>
          <div style={{position:"absolute",top:"20px",left:"30px",display:"flex",alignItems:"center"}}>
             <Link to="/" style={{
                display:"flex",
                gap:5,
                alignItems:"center",
                fontWeight:"bold",
                fontSize:"24px",
                textDecoration:"none",
            }}>
                <img src={assets.logo} alt="logo" height={25} width={25} className="me-2" style={{ display: "block" }}/>
                <span className="fw-bold fs-4 text-black">Authentication</span>
            </Link>
          </div>
          <div className="card p-4" style={{maxWidth:"400px", width:"100%"}}>
            <h2 className="text-center mb-4">
                {isCreateAccount ? "Create Account" : "Login"}
            </h2>
            <form>
                {
                    isCreateAccount &&(
                        <div className="mb-3">
                            <label htmlFor="name" className="form-label">Name : </label>
                            <input type="text"
                            id="name"
                            className="form-control"
                            placeholder="Enter your name"
                            required />
                        </div>
                    )
                }
                <div className="mb-3">
                    <label htmlFor="email" className="form-label">Email Id : </label>
                    <input type="text"
                    id="email"
                    className="form-control"
                    placeholder="Enter your email"
                    required  />
                </div>

                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password : </label>
                     <input type="text"
                    id="password"
                    className="form-control"
                    placeholder="Enter your password"
                    required  />
                </div>

                <div className="d-flex justify-content-between mb-3">
                    <Link to="/reset-password" className="text-decoration-none">
                    Forgot Password ?
                    </Link>
                </div>

                <button type="submit" className="btn btn-primary w-100"> Login </button>


            </form>
          </div>
        </div>
    )
}

export default Login;