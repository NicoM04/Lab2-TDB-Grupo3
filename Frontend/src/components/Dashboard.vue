<template>
  <div class="dashboard-container">
    <h1 class="bienvenida">¡Bienvenid@ a DocumentosYa!</h1>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column; 
  justify-content: center;
  align-items: center;
  height: 70vh;
  padding: 20px;
}

.bienvenida {
  text-align: center;
  font-size: 3rem;
  font-weight: 800;
  color: #ffffff;
  margin-top: 60px;
  letter-spacing: 2px;
}
</style>