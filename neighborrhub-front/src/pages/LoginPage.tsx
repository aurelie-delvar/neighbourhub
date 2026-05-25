import { useState } from "react"
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import api from "../api/axios";
import { AuthResponse } from "../types";

export default function LoginPage() {

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await api.post<AuthResponse>('/auth/login', { mail, password });
            login(response.data.token);
            navigate('/');
        } catch (err: any) {
            setError(err.response?.data.error ?? 'Identifiants incorrects');
        } finally {
            setLoading(false);
        }
    } 

    
    return <div>Login</div>
}