import React, { useState } from "react";
import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import "./Checkpoints.css";
import axios from "axios";
import { useQuery } from "react-query";

export default function Checkpoints() {
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
      <div className="Card">
        <textarea
          id="w3review"
          name="w3review"
          rows="2"
          cols="50"
          placeholder="SAVE YOUR PROGRESS"
        ></textarea>

        <hr></hr>
        <input type="submit" value="SAVE" />
      </div>
      <div className="Cards">
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
