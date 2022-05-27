import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignUp.css'

const SignUp = () => {
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
                            <button className="btn" type="submit">REGISTER</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        </>
    )
}

export default SignUp;