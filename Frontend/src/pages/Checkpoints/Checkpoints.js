import React, { useContext, useEffect, useState } from "react";
import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import "./Checkpoints.css";
import { UserContext } from "../../context/UserContext";
import { useHistory } from "react-router-dom";
import axios from "axios";
import { useQuery } from "react-query";
import { UserContext } from "../../context/UserContext";

export default function Checkpoints() {
  const [content, setContent] = useState("");
  let history = useHistory();
  const User = useContext(UserContext)[0];
  const setUser = useContext(UserContext)[1];
  const { status, data } = useQuery(
    "fetch-checkpoints",
    () => fetchCheckpoints(),
    {
      refetchInterval: 5000,
    }
  );

  async function fetchCheckpoints() {
    return await axios.get(`/checkpoints`).then((res) => {
      return res.data;
    });
  }
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const access_token = params.get("access_token");
    if (access_token) {
      localStorage.setItem("token", access_token);
      setUser(true);
      history.push("/checkpoints");
    }
  }, []);

  function handleChange(e) {
    setContent(e.target.value);
  }

  function saveCheckpoint() {
    if (content && content.length > 0) {
      axios({
        method: "post",
        url: "/api/checkpoints",
        data: { content },
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      })
        .then((res) => setContent(""))
        .catch((err) => console.log(err.message));
    }
  }

  if (status === "loading") {
    return <p style={{ color: "white" }}>Loading ...</p>;
  }
  return (
    <>
      <div className="background">
        <img src={background} alt="background" />
      </div>

      <div className="grid">
        <Logo />
        <Navbar links={routes} />
      </div>

      <div className="Cards">
        <h1 style={{ color: "white" }}>Checkpoints</h1>
        {User && (
          <div className="Card">
            <textarea
              onChange={handleChange}
              id="content"
              name="content"
              rows="2"
              cols="50"
              placeholder="SAVE YOUR PROGRESS"
              value={content}
            ></textarea>

            <hr></hr>
            <button className="save-button" onClick={saveCheckpoint}>
              SAVE
            </button>
          </div>
        )}
        {data.map((checkpoint) => {
          return (
            <CheckpointCard
              date={checkpoint.formattedCheckpointDate}
              username={checkpoint.username}
              content={checkpoint.content}
              id={checkpoint.id}
              key={checkpoint.id}
              isShared={false}
            />
          );
        })}
      </div>

      <div className="footer">
        <img src={darkSools} alt="dark sools" />
        <span>THE INTERNET CHECKPOINT | TICP TEAM 2022</span>
      </div>
    </>
  );
}
