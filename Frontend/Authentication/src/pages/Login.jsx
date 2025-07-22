import { assets } from "../assets/assets";
import { Link } from "react-router-dom";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { AppConstants } from "../util/constant";    
import axios from "axios";


const Login = () =>{

    const navigate = useNavigate();
    const[isCreateAccount,setIsCreateAccount] = useState(false);
    const[name,setName] = useState("");
    const[email,setEmail] = useState("");
    const[password,setPassword] = useState("");
    const[loading,setLoading] = useState(false);

const onSubmitHandler = async (e) => {
  e.preventDefault();
  axios.defaults.withCredentials = true;
  setLoading(true);

  try {
    if (isCreateAccount) {
      const response = await axios.post(`${AppConstants.BACKEND_URL}/register`, {
        name,
        email,
        password,
      });

      if (response.status === 201) {
        navigate("/");
        toast.success("Account created successfully, please login to continue");
      } else {
        toast.error("Email already exists");
      }
    } else {
      // TODO: Add login API call here if needed
    }
  } catch (err) {
    toast.error("Error creating account, please try again later");
  } finally {
    setLoading(false);
  }
};


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

            <form onSubmit={onSubmitHandler}>
                {
                    isCreateAccount &&(
                        <div className="mb-3">
                            <label htmlFor="name" className="form-label">Name : </label>
                            <input type="text"
                            id="name"
                            className="form-control"
                            placeholder="Enter your name"
                            required 
                            onChange={(e)=>setName(e.target.value)} value={name}
                            />
                        </div>
                    )
                }
                <div className="mb-3">
                    <label htmlFor="email" className="form-label">Email Id : </label>
                    <input type="text"
                    id="email"
                    className="form-control"
                    placeholder="Enter your email"
                    required 
                    onChange={(e)=>setEmail(e.target.value)} value={email}
                     />
                </div>

                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password : </label>
                     <input type="text"
                    id="password"
                    className="form-control"
                    placeholder="Enter your password"
                    required
                    onChange={(e)=>setPassword(e.target.value)} value={password}
                    />
                </div>

                <div className="d-flex justify-content-between mb-3">
                    <Link to="/reset-password" className="text-decoration-none">
                    Forgot Password ?
                    </Link>
                </div>

                <button type="submit" className="btn btn-primary w-100" disabled={loading}> 
                    {loading ? "Loading..." : isCreateAccount ? "Sign Up" : "Login"}
                </button>


            </form>

            <div className="text-center mt-3">
                <p className="mb-0">
                    {
                        isCreateAccount ? 
                      (
                          <>
                        Already have an account?{" "}
                        <span 
                        onClick={()=>setIsCreateAccount(false)}
                        className="text-decoration-underline" style={{cursor:"pointer"}}>
                            Login Here
                        </span>
                        </> 
                      ):
                      <>
                        Don't have an account?{" "}
                        <span 
                        onClick={()=>setIsCreateAccount(true)}
                        className="text-decoration-underline" style={{cursor:"pointer"}}>
                            Sign Up
                        </span>
                    </>
                    }
                </p>
            </div>
          </div>
        </div>
    )
}

export default Login;