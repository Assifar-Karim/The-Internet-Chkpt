import React, { useState, useEffect } from "react";
import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import axios from "axios";
import "./MyCheckpoints.css";
import { useParams } from "react-router-dom";
import useAxiosPrivate from "../../hooks/useAxiosPrivate";

export default function MyCheckpoints() {
  const params = useParams();
  const [data, setData] = useState(null);
  const privateAxios = useAxiosPrivate();
  
  useEffect(() => {
    const fetchCheckpoints = async () => {
      if(params && params.username)
      {
        try
        {
          const response = await axios.get(`/checkpoints/user/${params.username}`);
          setData(response?.data);  
        }
        catch(err)
        {
          console.error("Could not load checkpoints");
          setData([]);
        }
      }
      else
      {
        try
        {
          const response = await privateAxios.get("/api/checkpoints/my-checkpoints");
          setData(response?.data);
        }
        catch(err)
        {
          console.error("Could not load checkpoints");
          setData([]);
        }
      }
    }
    fetchCheckpoints();
  }, []);

  if (!data)
  {
    return <p style={{ color: "white" }}>Loading ...</p>;
  }

  return (
    <>
      <div className="background-chkpt">
        <img src={background} alt="background" />
      </div>

      <div className="grid">
        <Logo />
        <Navbar links={routes} />
      </div>

      <div className="Cards">
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
          <h1 style={{ color: "white" }}>
            {params && params.username
              ? `${params.username}'s checkpoints`
              : "My checkpoints"}
          </h1>
      </div>

      <div className="footer">
        <img src={darkSools} alt="dark sools" />
        <span>THE INTERNET CHECKPOINT | TICP TEAM 2022</span>
      </div>
    </>
  );
}
