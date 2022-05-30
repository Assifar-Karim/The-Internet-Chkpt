import background from "../../assets/backgrounds/shovel-knight-bg1.gif";
import memcard from "../../assets/icons/memcard.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import "./MainPage.css";
import { routes } from "../../Routes";
import { useHistory } from "react-router-dom";


const MainPage = () => {
  let history = useHistory();

  return (
    <>
      <div className="background">
        <img src={background} alt="background" />
      </div>
      <div className="grid">
        <Logo />
        <Navbar links={routes} />
        <div className="content">
          <span id="big-title">
            WELCOME WEARY TRAVELER TO THE INTERNET CHECKPOINT
          </span>
          <span id="small-title">
            IF YOU FOUND THIS PLACE YOU ARE A MAIN CHARACTER
          </span>
          <button type="button" onClick={() => history.push("/sign-in")}>
            <img src={memcard} alt="memcard" />
            START
          </button>
        </div>
      </div>
    </>
  );
};

export default MainPage;
