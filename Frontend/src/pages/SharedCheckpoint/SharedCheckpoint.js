import React, { useEffect, useState } from "react";
import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import "./SharedCheckpoint.css";
import { useParams, useHistory } from "react-router-dom";
import axios from "axios";

export default function SharedCheckpoint() {
  const [checkpoint, setCheckpoints] = useState({});
  const params = useParams();
  const history = useHistory();

  useEffect(() => {
    axios
      .get(`/checkpoints/${params.id}`)
      .then((res) => setCheckpoints(res.data))
      .catch((err) => history.push("/404"));
  }, []);

  return (
    <>
      <div className="background">
        <img src={background} alt="background" />
      </div>

      <div className="grid">
        <Logo />
        <Navbar links={routes} />
      </div>

      <div className="checkpoint-card">
        <CheckpointCard
          id={checkpoint.id}
          username={checkpoint.username}
          content={checkpoint.content}
          date={checkpoint.formattedCheckpointDate}
        />
      </div>

      <div className="footer">
        <img src={darkSools} alt="clock" />
        <span>THE INTERNET CHECKPOINT | TICP TEAM 2022</span>
      </div>
    </>
  );
}
