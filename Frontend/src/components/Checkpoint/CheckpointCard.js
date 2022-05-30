import "./CheckpointCard.css";
import clock from "../../assets/icons/clock.png";
import users from "../../assets/icons/users.png";
import deleteIcon from "../../assets/icons/delete.png";
import axios from "axios";
import { Link } from "react-router-dom";

const CheckpointCard = ({
  id,
  username,
  date,
  content,
  isShared = true,
  deleteChkpt = false,
}) => {
  function copyLinkToClipBoard() {
    navigator.clipboard
      .writeText(`${window.location}/${id}`)
      .then(null, function (err) {
        alert("Async: Could not copy text: ", err);
      });
  }

  function deleteCheckpoint(id) {
    axios({
      method: "delete",
      url: `/api/checkpoints/${id}`,
      data: { content },
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
  }
  return (
    <div className="Card">
      <div className="Card-header">
        <span>
          <Link
            style={{ color: "white", textDecoration: "none" }}
            to={`/user/${username}`}
          >
            {username}
          </Link>
        </span>
        <div className={deleteChkpt ? "time-4" : "time"}>
          <img className="clockImg" src={clock} alt="time" />
          <span>{date}</span>
          {!isShared && (
            <img
              onClick={() => copyLinkToClipBoard(id)}
              className="share-icon clockImg"
              src={users}
              alt="share icon"
            />
          )}
          {deleteChkpt && (
            <img
              onClick={() => deleteCheckpoint(id)}
              className="share-icon clockImg"
              src={deleteIcon}
              alt="delete icon"
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
