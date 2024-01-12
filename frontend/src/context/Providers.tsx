"use client"

import React from "react"
import { BetsProvider } from "./CouponBetsContext"
import { AuthProvider } from "./AuthContext"


export default function Providers({children,}: {children: React.ReactNode}) {
    return (
        <>
        <BetsProvider>
        <AuthProvider>
        {children}
         </AuthProvider>
        </BetsProvider>
        </>
    )
}