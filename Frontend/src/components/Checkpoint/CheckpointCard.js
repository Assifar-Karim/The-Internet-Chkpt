import "./CheckpointCard.css";
import clock from "../../assets/icons/clock.png";

const CheckpointCard = () => {
    return(
        <div className="Card" >
            <div className="Card-header">
                <span>Checkpoint</span>
                <div className="time">
                    <img className="clockImg" src={clock} alt="time" />
                    <span>12/05/2022</span>
                    <span>12:00</span>
                </div>
            </div>
            <hr></hr>
            <p>Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2Checkpoint 2</p>
        </div>
    );
}

export default CheckpointCard;