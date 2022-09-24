import { useRef, useState } from "react";
import "./AudioPlayer.css";
import aubenwelt from "../../assets/sounds/aubenwelt.mp3";
import bramble_blast from "../../assets/sounds/bramble_blast.mp3";
import corridors_of_time from "../../assets/sounds/corridors_of_time.mp3";
import play_icon from "../../assets/icons/play.png";
import pause_icon from "../../assets/icons/pause.png";
import next_icon from "../../assets/icons/next.png";
import back_icon from "../../assets/icons/back.png";

const AudioPlayer = () => {
    const songs = [aubenwelt, bramble_blast, corridors_of_time];
    const [currentSong, setCurrentSong] = useState({"song": songs[0], "index": 0});
    const [isPlaying, setIsPlaying] = useState(false);
    const audioRef = useRef(null);
    
    const togglePlayPause = () => {
        if(isPlaying)
        {
            audioRef.current.pause();
        }
        else
        {
            audioRef.current.play();
        }
        setIsPlaying(!isPlaying);
    };

    const changeTrackHandler = async direction => {
        if(direction === "forward")
        {
            const index = (currentSong["index"] + 1) % songs.length;
            await setCurrentSong({"song": songs[index], "index": index});

        }
        else if(direction === "back")
        {
            const index = (currentSong["index"] - 1) % songs.length;
            await setCurrentSong({"song": songs[index], "index": index});
        }

        if(isPlaying)
        {
            audioRef.current.play();
        }
    };
    
    return(
        <>
            <div className="Player-container">
                <img className="prev" src={back_icon} onClick={() => changeTrackHandler("back")}/>
                <img className="play" src={isPlaying ? pause_icon: play_icon} onClick={togglePlayPause}/>
                <img className="next" src={next_icon} onClick={() => changeTrackHandler("forward")}/>
            </div>
            <audio ref={audioRef} src={currentSong["song"]} loop>
            </audio>
        </>
    );
}

export { AudioPlayer }