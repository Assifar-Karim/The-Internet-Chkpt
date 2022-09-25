import logo from "../../assets/icons/Internet-Checkpoint-logo-cropped.gif";
import "./SignIn.css";
import axios from "axios";
import { useHistory } from "react-router-dom";
import { useEffect, useState } from "react";
import GoogleLogo from "../../assets/icons/google_pa.png";
import useUser from "../../hooks/useUser";

const SignIn = () => {
  let uri = `/oauth2/authorize/google?redirect_uri=${window.location.origin}/checkpoints`;

  let history = useHistory();
  const user = useUser()[0];
  const setUser = useUser()[1];
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errMsg, setErrMsg] = useState("");

  useEffect(() => {
    if (user) {
      history.push("/checkpoints");
    }
  }, [user]);

  const redirectToSignUP = () => {
    history.push("/sign-up");
  };

  const handleNormalSubmit = async (e) => {
    e.preventDefault();
    let bodyFormData = new FormData();
    bodyFormData.append("username", username);
    bodyFormData.append("password", password);
    try
    {
      const response = await axios.post("/back/login", bodyFormData, {
        headers: {"Content-Type": "application/json"},
        withCredentials: true
      });
      const accessToken = response?.data["access_token"];
      const refreshToken = response?.data["refresh_token"];
      setUser({accessToken});
      localStorage.setItem("token", refreshToken);
      history.push("/checkpoints");
    }
    catch(err)
    {
      if(!err?.response)
      {
        setErrMsg("NO SERVER RESPONSE");
      }
      else if(err.response?.status === 401)
      {
        setErrMsg("INVALID USERNAME OR PASSWORD");
      }
      else
      {
        setErrMsg("LOGIN FAILED");
      }
    }
  };

  return (
    <>
      <div className="wrapper">
        <div className="sin-background"></div>
        <div className="sin-side-bar">
          <div className="sin-logo" onClick={() => history.push("/")}>
            <img src={logo} alt="TICP Logo" />
          </div>
          <div className="sin-content">
            <div id="sin-heading-1">{errMsg === "" ? "LOG IN TO YOUR ACCOUNT" : errMsg}</div>
            <form className="sin-form">
              <div>
                <label htmlFor="username">USERNAME</label>
                <input id="username" type="text" onChange={e => setUsername(e.target.value)} value={username} required />
              </div>
              <div>
                <label htmlFor="password">PASSWORD</label>
                <input id="password" type="password" onChange={e => setPassword(e.target.value)} value={password} required/>
              </div>
              <div className="">
                <button
                  className="btn"
                  type="submit"
                  onClick={handleNormalSubmit}
                >
                  LOAD SAVE
                </button>
              </div>
            </form>
            <div id="sin-heading-2">
              NOT A MAIN CHARACTER YET ?
              <span id="sin-special-text" onClick={redirectToSignUP}>
                SIGNUP
              </span>
            </div>
          </div>
          <div className="line"></div>
          <p className="or">OR</p>
          <div className="btn btn-container oauth">
            <img src={GoogleLogo} id="google" />
            <a
              className=""
              href={uri}
              style={{ textDecoration: "none", color: "white" }}
            >
              LOAD YOUR GOOGLE SAVE
            </a>
          </div>
        </div>
      </div>
    </>
  );
};

export default SignIn;
