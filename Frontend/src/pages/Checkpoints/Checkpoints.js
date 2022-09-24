import React, { useContext, useEffect, useState } from "react";
import background from "../../assets/backgrounds/main-bg.png";
import Logo from "../../components/Logo/Logo";
import Navbar from "../../components/Navbar/Navbar";
import CheckpointCard from "../../components/Checkpoint/CheckpointCard";
import { routes } from "../../Routes";
import darkSools from "../../assets/icons/dark-souls-bonfire.gif";
import "./Checkpoints.css";
import { useHistory } from "react-router-dom";
import axios from "axios";
import { useQuery } from "react-query";
import { UserContext } from "../../context/UserContext";
import useAxiosPrivate from "../../hooks/useAxiosPrivate";
import SockJS from "sockjs-client";
import { Stomp } from "stomp-websocket/lib/stomp";

export default function Checkpoints() {
  const [content, setContent] = useState("");
  let history = useHistory();
  const user = useContext(UserContext)[0];
  const setUser = useContext(UserContext)[1];
  const axiosPrivate = useAxiosPrivate();

  const [wsData, setWsData] = useState(null);
  const { status, data } = useQuery(
    "fetch-checkpoints",
    () => fetchCheckpoints(),
    user ? { refetchOnWindowFocus: false, onSuccess: setWsData } : { refetchInterval: 5000 }
  );
  
  
  async function fetchCheckpoints() {
    return await axios.get(`/checkpoints`).then((res) => {
      return res.data;
    });
  }
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get("access_token");
    const refreshToken = params.get("refresh_token");
    if (accessToken && refreshToken)
    {
      localStorage.setItem("token", refreshToken);
      setUser({ accessToken });
      history.push("/checkpoints");
    }
  }, []);

  useEffect(() => {
    if(user && wsData)
    {
      const ws = new SockJS("http://localhost:8080/ws-checkpoints");
      const stompClient = Stomp.over(ws);
      stompClient.debug = null;
      stompClient.connect({"token": user.accessToken}, frame => {
        stompClient.subscribe('/topic/checkpoints', message => {
          const receivedMsg = JSON.parse(message.body);
          if(receivedMsg["id"] && receivedMsg["username"] && receivedMsg["content"] && receivedMsg["formattedCheckpointDate"])
          {
            setWsData( prev => {
              return [...prev, receivedMsg]
            });
          }
        });
      });

      return () => ws.close();
    }
  }, [user, data]);

  function handleChange(e) {
    setContent(e.target.value);
  }

  const saveCheckpoint = async () => {
    if (content && content.length > 0)
    {
      try
      {
        const response = await axiosPrivate.post("/api/checkpoints", {"content": content});
        setContent("");

      }
      catch(err)
      {
        if(!err?.response)
        {
          console.log("No server response");
        }
        else if(err.response?.status === 401 || err.response?.status === 403)
        {
          console.log("Could not write the checkpoint");
        }
      }
    }
  }

  if (status === "loading" || (user && !wsData)) {
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
        {user ? wsData.map((checkpoint) => {
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
        })
      :
      data.map((checkpoint) => {
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
      })
      }
        {user && (
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
        <h1 style={{ color: "white" }}>Checkpoints</h1>
      </div>

      <div className="footer">
        <img src={darkSools} alt="dark sools" />
        <span>THE INTERNET CHECKPOINT | TICP TEAM 2022</span>
      </div>
    </>
  );
}
