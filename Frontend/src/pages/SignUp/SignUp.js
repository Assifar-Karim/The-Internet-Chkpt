import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignUp.css'
import axios from 'axios'



const SignUp = () => {

    const handleSubmit = async (e) => {
        e.preventDefault();
        const email = e.target.form[0].value;
        const username = e.target.form[1].value;
        const password = e.target.form[2].value;
        const registerFormData = {"email":email,"username":username,"password":password};
        console.log(registerFormData)
        try {
            // make axios post request
           await axios({
              method: "post",
              url: "http://localhost:8080/register",
              data: registerFormData,
              headers:{"Content-Type": "application/json"}
            });
          } catch(error) {
            console.log(error)
          }
    }

    return (
        <>
        <div className="wrapper">
            
            <div className="sin-background"></div>

            <div className="sin-side-bar">
                <div className="sin-logo">
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
                </div>
            </div>
        </div>
        </>
    )
}

export default SignUp;