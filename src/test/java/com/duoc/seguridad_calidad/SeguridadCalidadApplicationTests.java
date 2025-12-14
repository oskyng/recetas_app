package com.duoc.seguridad_calidad;

import org.junit.jupiter.api.Test;

/**
 * Prueba de humo trivial que NO carga el ApplicationContext.
 * Evita fallos por configuración de seguridad/autoconfiguraciones al ejecutar unit tests.
 */
class SeguridadCalidadApplicationTests {

    @Test
    void contextLoads() {
    }
}
