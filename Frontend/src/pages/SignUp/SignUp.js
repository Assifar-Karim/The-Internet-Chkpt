import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignUp.css'
import axios from 'axios'
import { useHistory } from "react-router-dom";
import { useState } from "react";


const SignUp = () => {
    const [status, setStatus] = useState({type:''});
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errMsg, setErrMsg] = useState("");

    

    let history = useHistory();

    const handleSubmit =  async e => {
        setStatus({type:''})
        e.preventDefault();
        const email = e.target.form[0].value;
        const username = e.target.form[1].value;
        const password = e.target.form[2].value;
        const registerFormData = {"email":email,"username":username,"password":password};
        
        // make axios post request
        try
        {
            const response = await axios.post("/users", registerFormData, {
                headers: {"Content-Type": "application/json"}
            });
            setPassword("");    
            setStatus({ type: "Success" });
        }
        catch(err)
        {
            if(!err?.response)
            {
                setErrMsg("No server response");
            }
            else if(err.response?.status === 409)
            {
                setErrMsg(err.response?.data?.message);
            }
            else
            {
                setErrMsg("Login failed");
            }
            setStatus({ type: "Error" });
        } 
    }

    return (
        <>
        <div className="wrapper">
            
            <div className="sin-background"></div>

            <div className="sin-side-bar">
                <div className="sin-logo" onClick={() => history.push("/")}>
                    <img src={logo} alt="TICP Logo" />
                </div>
                <div className="sin-content">
                    <div id="sup-heading-1">REGISTER IN THE MAIN CHARACTER GUILD</div>
                </div>
                <div className="sin-content">
                    <form className="sin-form">
                        <div>
                            <label htmlFor="sup-email">EMAIL</label>
                            <input id="sup-email" type="email" onChange={e => setEmail(e.target.value)} value={email} required/>
                        </div>
                        <div>
                            <label htmlFor="sup-username">USERNAME</label>
                            <input id="sup-username" type="text" onChange={e => setUsername(e.target.value)} value={username} required/>
                        </div>
                        <div>
                            <label htmlFor="sup-password">PASSWORD</label>
                            <input id="sup-password" type="password" onChange={e => setPassword(e.target.value)} value={password} required/>
                        </div>
                        <div className="">
                            <button className="btn" type="submit" onClick={handleSubmit}>REGISTER</button>
                        </div>
                    </form>
                    {status.type === 'Success' &&  <div className="alert success">Registration Succeeded. Please verify your account !</div>}
                    {status.type ===  'Error'  &&  <div className="alert error">{errMsg}</div>}
                </div>
            </div>
        </div>
        </>
    )
}

export default SignUp;