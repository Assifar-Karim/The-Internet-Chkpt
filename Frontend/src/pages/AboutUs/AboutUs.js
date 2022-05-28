import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import "../SharedCheckpoint/SharedCheckpoint.css";

export default function AboutUs()
{
  const checkpoint = { 
    id: "1",
    username: "THE INTERNET CHECKPOINT TEAM / SLOT N°1",
    content: "WE ARE A SMALL TEAM OF FIVE SOFTWARE ENGINEERING STUDENTS WHO SET OUT TO RECREATE ,AS AN HOMAGE, THE FAMOUS INTERNET CHECKPOINT THAT USED TO BE FOUND ON TAIA777'S YOUTUBE CHANNEL BUT THAT UNFORTUNATELY GOT TAKEN DOWN RECENTLY",
    date: "28/15/2022 18:25"
};
  return (
    <>
      <div className="background">
        <img src={background} alt="background" />
      </div>

      <div className="grid">
        <Logo />
        <Navbar links={routes} />
      </div>
      <div className="checkpoint-card" style={{textAlign: "center"}}>
        <CheckpointCard
          id={checkpoint.id}
          username={checkpoint.username}
          content={checkpoint.content}
          date={checkpoint.date}
        />
      </div>

      <div className="footer">
        <img src={darkSools} alt="clock" />
        <span>THE INTERNET CHECKPOINT | TICP TEAM 2022</span>
      </div>
    </>
  );
}
