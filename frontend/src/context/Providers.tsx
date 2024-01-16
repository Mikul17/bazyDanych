"use client"

import React from "react"
import { BetsProvider } from "./CouponBetsContext"
import { AuthProvider } from "./AuthContext"
import { BalanceProvider } from "./BalanceContext"


export default function Providers({children,}: {children: React.ReactNode}) {
    return (
        <>
        <BetsProvider>
        <AuthProvider>
        <BalanceProvider>
        {children}
        </BalanceProvider>
         </AuthProvider>
        </BetsProvider>
        </>
    )
}