import { Link } from "react-router-dom";
import "./Navbar.css";

const Navbar = ({ links }) => {
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
      </ul>
      <hr />
    </div>
  );
};

export default Navbar;
