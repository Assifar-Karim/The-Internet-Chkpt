import React, { useState, useEffect } from "react";
import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import axios from "axios";
import "./MyCheckpoints.css";
import { useQuery } from "react-query";
import { useParams } from "react-router-dom";

export default function MyCheckpoints() {
  const params = useParams();
  const { status, data } = useQuery(
    "fetch-checkpoints",
    () => fetchCheckpoints(),
    {
      refetchInterval: 10,
    }
  );

  async function fetchCheckpoints() {
    if (params && params.username) {
      return await axios({
        method: "get",
        url: `/checkpoints/user/${params.username}`,
      }).then((res) => {
        return res.data;
      });
    }
    return await axios({
      method: "get",
      url: "/api/checkpoints/my-checkpoints",
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    })
      .then((res) => {
        return res.data;
      })
      .catch((err) => console.log(err.message));
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
        <h1 style={{ color: "white" }}>
          {params && params.username
            ? `${params.username}'s checkpoints`
            : "My checkpoints"}
        </h1>
        {data &&
          data.map((checkpoint) => {
            return (
              <CheckpointCard
                date={checkpoint.formattedCheckpointDate}
                username={checkpoint.username}
                content={checkpoint.content}
                id={checkpoint.id}
                key={checkpoint.id}
                isShared={false}
                deleteChkpt={params && params.username ? false : true}
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
