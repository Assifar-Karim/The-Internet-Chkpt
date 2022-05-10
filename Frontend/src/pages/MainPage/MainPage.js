import background from "../../assets/backgrounds/shovel-knight-bg1.gif";
import memcard from "../../assets/icons/memcard.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import "./MainPage.css"

const MainPage = () => {
    const links = [{"title": "Checkpoints"}, {"title": "About Us"}]
    return(
        <>
            <div className="background">
                <img src={background} alt="background"/>
            </div>
            <div className="grid">
                <Logo/>
                <Navbar links={links} />
                <div className="content">
                    <span id="big-title">WELCOME WEARY TRAVELER TO THE INTERNET CHECKPOINT</span>
                    <span id="small-title">IF YOU FOUND THIS PLACE YOU ARE A MAIN CHARACTER</span>
                    <button type="button"><img src={memcard} alt="memcard" />START</button>
                </div>
            </div>
            
        </>
    );
}

export default MainPage;