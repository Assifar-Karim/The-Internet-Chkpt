import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignIn.css'

const SignIn = () => {
    return (
        <>
            <div className="wrapper">
                <div className="sin-background">
                    {/* <img src={background} alt="knight alone in the woods sitting in front of fire"/> */}
                </div>

                <div className="sin-side-bar">
                    <div className="sin-logo">
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
                                <button className="btn" type="submit">LOAD SAVE</button>
                            </div>
                        </form>
                        <div id="sin-heading-2">
                            NOT A MAIN CHARACTER YET ? 
                            <span id="sin-special-text">SIGNUP</span>
                        </div>
                    </div>
                        <div className="line"></div>
                        <p className="or">OR</p>
                    <div className="btn-container">
                        <button type="submit" className="btn">LOAD YOUR GOOGLE SAVE</button>
                    </div>
                </div>
            </div>
        </>
    )
}

export default SignIn;