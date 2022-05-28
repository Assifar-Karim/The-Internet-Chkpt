import "./CheckpointCard.css";
import clock from "../../assets/icons/clock.png";
import users from "../../assets/icons/users.png";

const CheckpointCard = ({ id, username, date, content, isShared = true }) => {
  function copyLinkToClipBoard() {
    console.log(window.location);
    navigator.clipboard
      .writeText(`${window.location}/${id}`)
      .then(null, function (err) {
        alert("Async: Could not copy text: ", err);
      });
  }

  return (
    <div className="Card">
      <div className="Card-header">
        <span>{username}</span>
        <div className="time">
          <img className="clockImg" src={clock} alt="time" />
          <span>{date}</span>
          {!isShared && (
            <img
              onClick={() => copyLinkToClipBoard(id)}
              className="share-icon clockImg"
              src={users}
            />
          )}
        </div>
      </div>
      <hr></hr>
      <p>{content}</p>
    </div>
  );
};

export default CheckpointCard;
