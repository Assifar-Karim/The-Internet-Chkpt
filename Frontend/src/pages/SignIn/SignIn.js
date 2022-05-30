import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignIn.css'
import axios from "axios"
import { useHistory } from "react-router-dom";
import { useContext, useEffect } from "react"; 
import { UserContext } from '../../context/UserContext';

const SignIn = () => {

    let uri = process.env.REACT_APP_BASE_URL+"/oauth2/authorize/google?redirect_uri="+process.env.REACT_APP_BASE_URL.split(":")[0]+":"+process.env.REACT_APP_BASE_URL.split(":")[1]+":3000/checkpoints";
    
    let history = useHistory();
    const User = useContext(UserContext)[0];
    const setUser = useContext(UserContext)[1];


    useEffect(() => {
        if(User){
            history.push("/")
        }
    },[User]);

    const redirectToSignUP = () => {
        history.push("/sign-up")
    }
    
    const handleNormalSubmit = (e) => {
        e.preventDefault();
        let bodyFormData = new FormData();
        bodyFormData.append("username",e.target.form[0].value);
        bodyFormData.append("password",e.target.form[1].value);
        
        // make axios post request
        axios({
            method: "post",
            url: "http://localhost:8080/login",
            data: bodyFormData,
        }).then(res => {
            localStorage.setItem("token",res.data["access_token"])
            setUser(true)
            history.push("/checkpoints")
        }).catch(err => {
            console.log(err);
        });
    }


    return (
        <>
            <div className="wrapper">
                <div className="sin-background">
                    {/* <img src={background} alt="knight alone in the woods sitting in front of fire"/> */}
                </div>

                <div className="sin-side-bar">
                    <div className="sin-logo" onClick={() => history.push("/")}>
                        <img src={logo} alt="TICP Logo" />
                    </div>
                    <div className="sin-content">
                        <div id="sin-heading-1">
                                LOG IN TO YOUR ACCOUNT
                        </div>
                        <form className="sin-form">
                            <div>
                                <label htmlFor="username">USERNAME</label>
                                <input id="username" type="text"/>
                            </div>
                            <div>
                                <label htmlFor="password">PASSWORD</label>
                                <input id="password" type="password"/>
                            </div>
                            <div className="">
                                <button className="btn" type="submit" onClick={handleNormalSubmit}>LOAD SAVE</button>
                            </div>
                        </form>
                        <div id="sin-heading-2">
                            NOT A MAIN CHARACTER YET ? 
                            <span id="sin-special-text" onClick={redirectToSignUP}>SIGNUP</span>
                        </div>
                    </div>
                        <div className="line"></div>
                        <p className="or">OR</p>
                    <div className="btn-container">
                        <a className="btn" href={uri} style={{textDecoration: 'none' }}>LOAD YOUR GOOGLE SAVE</a>
                    </div>
                </div>
            </div>
        </>
    )
}

export default SignIn;