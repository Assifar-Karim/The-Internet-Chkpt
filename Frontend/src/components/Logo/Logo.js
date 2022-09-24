import { useHistory } from "react-router-dom";
import logo from "../../assets/icons/Internet-Checkpoint-logo.gif"
import "./Logo.css"

const Logo = () => {
    const history = useHistory();
    const onClickEvent = () => {
        history.push("/");
    }
    return(
        <div className="logo" onClick={onClickEvent}>
            <img src={logo} alt="TICP Logo" />
        </div>
    );
}

export default Logo;