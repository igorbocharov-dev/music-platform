export async function registration(formData){
    return  await fetch("http://localhost:8081/api/auth/registration", {
        method: "POST",
        credentials: "include",
        body: formData
    });
}