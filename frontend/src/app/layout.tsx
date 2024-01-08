"use client";
import { Dosis, Inter } from 'next/font/google'
import './globals.css'
import { AuthProvider } from '@/context/AuthContext';
import { BetsProvider } from '@/context/CouponBetsContext';


const dosisFont = Dosis({
  weight: ["200", "300", "400", "500", "600", "700", "800"],
  style: ["normal"],
  subsets: ['latin'],
  display:'swap'
});

export default function RootLayout({children,}: {children: React.ReactNode}) {
  return (
    <html lang="en">
      <BetsProvider>
      <AuthProvider>
      <body className={dosisFont.className} style={{margin:"0", padding:"0", top:"0"}}>
          {children}
       </body>
       </AuthProvider>
       </BetsProvider>
    </html>
  )
}
