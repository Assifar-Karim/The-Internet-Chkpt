import { Link } from "react-router-dom";
import "./Navbar.css";
import { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import useUser from "../../hooks/useUser";
import { AudioPlayer } from "../AudioPlayer/AudioPlayer";

const Navbar = ({ links }) => {
  const user = useUser()[0];
  const setUser = useUser()[1];
  let history = useHistory();
  const [filtredRoutes, setFiltredRoutes] = useState([]);

  const handle = () => {
    if (user) {
      history.push("/");
      localStorage.removeItem("token");
      setUser(null);
    } else {
      history.push("/sign-in");
    }
  };

  useEffect(() => {
    let newLinks = links.map((link) => {
      if(link.segment === "/my-checkpoints")
      {
        if (user)
        {
          link.renderToNav = true;
        }
        else
        {
          link.renderToNav = false;
        }

      }
      return link;
    });

    setFiltredRoutes(newLinks);
  }, [links]);

  return (
    <div className="navbar-container">
      <div className="navbar">
        <AudioPlayer/>
        {filtredRoutes
          .filter((link) => link.renderToNav)
          .map((link, idx) => {
            return (
              <li key={idx}>
                <Link to={link.segment}>{link.title}</Link>
              </li>
            );
          })}
        <li onClick={handle}>
          <span className="logout">{user ? "Logout" : "Login"}</span>
        </li>
      </div>
      <hr />
    </div>
  );
};

export default Navbar;
