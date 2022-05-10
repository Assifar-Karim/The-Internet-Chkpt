import "./Navbar.css"

const Navbar = ({links}) => {
    return(
        <div className="navbar">
            <ul>
                {links.map((link, idx) => {
                    return(<li key={idx}>{link.title}</li>)
                })}
            </ul>
            <hr/>
        </div>
    );
}

export default Navbar;