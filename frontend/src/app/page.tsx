"use client"
import { usePathname } from "next/navigation";
import dynamic from "next/dynamic";
import Layout from "./navLayout";


const NoSSR = dynamic(() => import('../app/layout'), { ssr: false })


export default function Home() {
  const path : string = usePathname();

  return (
    <>
    <Layout path={path}/>
    </>
  )
}