import "./App.css";
import AuthenticatedRoute from "./components/AuthenticatedRoute";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import MainPage from "./components/MainPage";
import AdminPage from "./components/AdminPage";
import OrderInfo from "./components/OrderInfo";
import AddOrEditGoods from "./components/AddOrEditGoods";
import OrdersList from "./components/OrdersList";
import FeedbackPage from "./components/FeedbackPage";

import {
   BrowserRouter as Router,
   Route,
   Switch,
} from "react-router-dom";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/" exact component={LoginPage} />
        <Route path="/register" exact component={RegisterPage} />
        <AuthenticatedRoute path="/orders" exact component={MainPage} />
        <AuthenticatedRoute path="/goods" exact component={AdminPage} />
        <Route path="/add-edit" component={AddOrEditGoods} />
        <Route path="/orders/:id" component={OrderInfo} />
        <Route path="/ordersList" component={OrdersList} />
        <Route path="/feedbacks/:orderId" component={FeedbackPage} />
      </Switch>
    </Router>
  );
}

export default App;
