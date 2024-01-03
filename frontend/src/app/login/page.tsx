import Layout from "../navLayout";
import LoginForm from "@/components/LoginPage/LoginForm";
import { headers } from "next/headers";



export default function Login() {
  const headersList = headers();
  const activePath = headersList.get("x-invoke-path");

    return (
      <>
      <Layout path={activePath?activePath:"/login"}/>
      <LoginForm/>
      </>
  
    )
}