import { Link } from "react-router-dom";
import "./Navbar.css";
import { useContext } from "react";
import { UserContext } from '../../context/UserContext';
import { useHistory } from "react-router-dom";



const Navbar = ({ links }) => {
  const User = useContext(UserContext)[0];
  const setUser = useContext(UserContext)[1];
  let history = useHistory();

  const handle = () => {
    if(User){
      localStorage.removeItem("token");
      history.push("/");
      setUser(false);
    }else{
      history.push("/sign-in");
    }
  }
  return (
    <div className="navbar">
      <ul>
        {links
          .filter((link) => link.renderToNav)
          .map((link, idx) => {
            return (
              <li key={idx}>
                <Link to={link.segment}>{link.title}</Link>
              </li>
            );
          })}
          <li onClick={handle}><Link>Log{User?"out":"in"}</Link></li>
      </ul>
      <hr />
    </div>
  );
};

export default Navbar;
