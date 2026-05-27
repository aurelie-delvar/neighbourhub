import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import api from "../api/axios";
import type { User } from "../types";

interface AuthContextType {
    token: string | null;
    isAuthenticated: boolean;
    login: (token: string) => void;
    logout: () => void;
    currentUser: User | null;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
    const [currentUser, setCurrentUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            if (token) {
                try {
                    const response = await api.get<User>('/users/me');
                    setCurrentUser(response.data);                     
                } catch {
                    logout();
                }
            }
        };
        fetchUser();
    }, [token]);

    const login = (newToken: string) => {
        localStorage.setItem('token', newToken);
        setToken(newToken);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
        setCurrentUser(null);
    }

    return (
        <AuthContext.Provider value={{ token, login, logout, isAuthenticated: !!token, currentUser }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth(): AuthContextType {
    const context = useContext(AuthContext);
    if (!context) throw new Error('useAuth doit être utilisé dans le AuthProvider');
    return context;
}