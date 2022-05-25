import background from "../../assets/backgrounds/shovel-knight-bg1.gif";
import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif"
import './SignIn.css'

const SignIn = () => {
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
                            LOG IN TO YOUR ACCOUNT
                    </div>
                    <form className="form">
                        <div>
                            <label>USERNAME</label>
                            <input type={Text}/>
                        </div>
                        <div>
                            <label>PASSWORD</label>
                            <input type={Text}/>
                        </div>
                        <div>
                            <button type="submit">LOAD SAVE</button>
                        </div>
                    </form>
                    <div id="heading-2">
                        NOT A MAIN CHARACTER YET ? 
                        <span id="special-text">SIGNUP</span>
                    </div>
                </div>
               
                    <div className="line"></div>
                    <p className="or">OR</p>
            
                <div className="content">
                    <button type="submit" id="load-button">LOAD YOUR GOOGLE SAVE</button>
                </div>
            </div>
        </>
    )
}

export default SignIn;