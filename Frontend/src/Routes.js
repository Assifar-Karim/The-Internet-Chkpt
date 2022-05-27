import AboutUs from "./pages/AboutUs/AboutUs";
import Checkpoints from "./pages/Checkpoints/Checkpoints";
import MainPage from "./pages/MainPage/MainPage";
import SharedCheckpoint from "./pages/SharedCheckpoint/SharedCheckpoint";

export const routes = [
  { title: "", segment: "/", component: MainPage, readerToNav: false },
  {
    title: "Checkpoints",
    segment: "/checkpoints",
    component: Checkpoints,
    renderToNav: true,
  },
  {
    title: "Shared checkpoint",
    segment: "/checkpoints/:id",
    component: SharedCheckpoint,
    renderToNav: false,
  },
  {
    title: "About Us",
    segment: "/about-us",
    component: AboutUs,
    renderToNav: true,
  },
];
