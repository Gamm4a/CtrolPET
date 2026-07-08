const btn = document.getElementById('btn-login');
btn.addEventListener('click',async function (e) {
    e.preventDefault();

    const correo = document.getElementById('correo').value;
    const contrasenia = document.getElementById('contrasenia').value;

    if(!correo || !contrasenia){
         alert("Por favor de completar todos los campos")
            return;
    }

     //en este se necesita mandar el token 
      const response = await fetch('/api/login', {
        method: "POST",
        headers: {"Content-Type": "application/json" 
                 
        },
        body: JSON.stringify({
          correo:correo,
          contrasenia:contrasenia

      })
    })
       const resultado = await response.json();
      //si es verdadero se deberia de guardar el token
       if(response.ok){
         
       }else{
          mensaje.innerHTML ="Ocurrió un error, verifica tus datos" ;
     }
})