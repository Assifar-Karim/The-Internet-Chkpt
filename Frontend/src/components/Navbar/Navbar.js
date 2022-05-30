import { Link } from "react-router-dom";
import "./Navbar.css";
import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../context/UserContext";
import { useHistory } from "react-router-dom";
import alex from "../../assets/sounds/alex-productions.mp3";
import aubenwelt from "../../assets/sounds/aubenwelt.mp3";
import bramble_blast from "../../assets/sounds/bramble_blast.mp3";
import orridors_of_time from "../../assets/sounds/orridors_of_time.mp3";
import play_icon from "../../assets/icons/play.png";
import pause_icon from "../../assets/icons/pause.png";
import next_icon from "../../assets/icons/next.png";
import back_icon from "../../assets/icons/back.png";

const Navbar = ({ links }) => {
  const User = useContext(UserContext)[0];
  const setUser = useContext(UserContext)[1];
  let history = useHistory();
  const [filtredRoutes, setFiltredRoutes] = useState([]);

  const handle = () => {
    if (User) {
      localStorage.removeItem("token");
      history.push("/");
      setUser(false);
    } else {
      history.push("/sign-in");
    }
  };

  useEffect(() => {
    let newLinks = links.map((link) => {
      if (!User && link.segment === "/my-checkpoints") link.renderToNav = false;
      return link;
    });

    setFiltredRoutes(newLinks);
  }, [links]);

  const musicTracks = [alex, aubenwelt, bramble_blast, orridors_of_time];

  const [trackIndex, setTrackIndex] = useState(0);
  const [track, setTrack] = useState(new Audio(musicTracks[trackIndex]));
  const [isPlaying, setStatePlay] = useState(false);

  track.autoplay = true;

  const handleClickPrevious = () => {
    track.pause();
    setStatePlay(true);
    setTrackIndex((currentTrack) =>
      currentTrack === 0 ? musicTracks.length - 1 : currentTrack - 1
    );
  };

  const playMusic = (isPlaying) => {
    if (isPlaying) {
      track.loop = true;
      track.play();
    } else {
      track.pause();
    }
  };
  const handleClickNext = () => {
    track.pause();
    setStatePlay(true);
    setTrackIndex((currentTrack) =>
      currentTrack < musicTracks.length - 1 ? currentTrack + 1 : 0
    );
  };

  const handelClickPlay = () => {
    track.loop = true;
    track.play();
    setStatePlay(true);
  };

  const handelClickPause = () => {
    track.pause();
    setStatePlay(false);
  };

  useEffect(() => {
    setTrack(new Audio(musicTracks[trackIndex + 1]));
  }, [trackIndex]);

  useEffect(() => {
    track.play();
  }, [track]);

  return (
    <div className="navbar-container">
      <div className="navbar">
        <div className="Player-container">
          <img className="prev" src={back_icon} onClick={handleClickPrevious} />
          {!isPlaying ? (
            <img className="play" src={play_icon} onClick={handelClickPlay} />
          ) : (
            <img className="play" src={pause_icon} onClick={handelClickPause} />
          )}
          <img className="next" src={next_icon} onClick={handleClickNext} />
        </div>
        {filtredRoutes
          .filter((link) => link.renderToNav)
          .map((link, idx) => {
            return (
              <li key={idx}>
                <Link to={link.segment}>{link.title}</Link>
              </li>
            );
          })}
        <li onClick={handle} className="logout">
          Log{User ? "out" : "in"}
        </li>
      </div>
      <hr />
    </div>
  );
};

export default Navbar;
