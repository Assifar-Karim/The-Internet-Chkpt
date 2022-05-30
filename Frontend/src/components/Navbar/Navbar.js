import { Link } from "react-router-dom";
import "./Navbar.css";
import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../context/UserContext";
import { useHistory } from "react-router-dom";

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
    links.map((link) => {
      if (!User && link.segment === "/my-checkpoints") link.renderToNav = false;
    });

    setFiltredRoutes(links);
  }, [links]);

  return (
    <div className="navbar">
      <ul>
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
      </ul>
      <hr />
    </div>
  );
};

export default Navbar;
