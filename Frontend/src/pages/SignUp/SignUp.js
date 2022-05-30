import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignUp.css'
import axios from 'axios'
import { useHistory } from "react-router-dom";
import { useState } from "react";


const SignUp = () => {
    const [status, setStatus] = useState({type:''});
    

    let history = useHistory();

    const handleSubmit =  (e) => {
        setStatus({type:''})
        e.preventDefault();
        const email = e.target.form[0].value;
        const username = e.target.form[1].value;
        const password = e.target.form[2].value;
        const registerFormData = {"email":email,"username":username,"password":password};
        console.log(registerFormData)
        
        // make axios post request
        axios({
            method: "post",
            url: "http://localhost:8080/register",
            data: registerFormData,
            headers:{"Content-Type": "application/json"}
        }).then((res) => {
            e.target.form[0].value = "";
            e.target.form[1].value = "";
            e.target.form[2].value = "";
            setStatus({ type: 'Success' });
            console.log(res)
        }).catch((error) => {
            setStatus({ type: 'Error'});
            console.log(error)
        });;
          
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
                            <input id="sup-email" type="email"/>
                        </div>
                        <div>
                            <label htmlFor="sup-username">USERNAME</label>
                            <input id="sup-username" type="text"/>
                        </div>
                        <div>
                            <label htmlFor="sup-password">PASSWORD</label>
                            <input id="sup-password" type="password"/>
                        </div>
                        <div className="">
                            <button className="btn" type="submit" onClick={handleSubmit}>REGISTER</button>
                        </div>
                    </form>
                    {status.type === 'Success' &&  <div className="alert success">Registration Succeed </div>}
                    {status.type ===  'Error'  &&  <div className="alert error">Registration Failed</div>}
                </div>
            </div>
        </div>
        </>
    )
}

export default SignUp;