import background from "../../assets/backgrounds/shovel-knight-bg1.gif";
import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignUp.css'

const SignUp = () => {
    return (
        <>
            <div className="background">
                <img src={background} alt="knight alone in the woods sitting in front of fire"/>
            </div>

            <div className="side-bar">
                <div className="slogo">
                    <img src={logo} alt="TICP Logo" />
                </div>

                <div className="content">
                    <div id="heading-1">
                            REGISTER IN THE MAIN CHARACTER GUILD
                    </div>
                    <form className="form">
                        <div>
                            <label for="email">EMAIL</label>
                            <input id="email" type="email" />
                        </div>
                        <div>
                            <label for="username">USERNAME</label>
                            <input id="username" type="text" />
                        </div>
                        <div>
                            <label for="password">PASSWORD</label>
                            <input id="password" type="password"/>
                        </div>
                        <div>
                            <button type="submit">REGISTER</button>
                        </div>
                    </form>
                   
                </div>
            </div>
        </>
    )
}

export default SignUp;