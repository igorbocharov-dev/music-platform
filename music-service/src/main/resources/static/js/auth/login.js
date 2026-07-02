export async function login(payload){
    return await fetch("http://localhost:8081/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(payload)
    });
}