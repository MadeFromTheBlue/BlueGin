use jni::JNIEnv;
use jni::objects::{JClass, JObject, JValue, JString};

use std::collections::HashMap;
use std::sync::Arc;

use glium::uniforms::{Uniforms, AsUniformValue, UniformValue};

use JGLContext;

lazy_static! {
    static ref datas: HashMap<u64, JUniformValues> = HashMap::new();
}

pub struct JUniformValues {
    list: Vec<(String, Arc<AsUniformValue + Sync + Send>)>
}

impl Uniforms for JUniformValues {
    fn visit_values<'a, F: FnMut(&str, UniformValue<'a>)>(&'a self, mut f: F) {
        for &(ref name, ref val) in &self.list {
            f(name, val.as_uniform_value());
        }
    }
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_blue_made_bluegin_Uniforms_native_1init(
    env: JNIEnv,
    class: JClass,
    id: JValue) {
    unimplemented!(); // panics across FFI, not good
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_blue_made_bluegin_Uniforms_native_1data(
    env: JNIEnv,
    class: JClass,
    id: JValue,
    name: JString,
    data_type: JString,
    data: JObject) {
    unimplemented!(); // panics across FFI, not good
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_blue_made_bluegin_Uniforms_native_1remove(
    env: JNIEnv,
    class: JClass,
    id: JValue,
    name: JString) {
    unimplemented!(); // panics across FFI, not good
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_blue_made_bluegin_Uniforms_native_1delete(
    env: JNIEnv,
    class: JClass,
    id: JValue) {
    unimplemented!(); // panics across FFI, not good
}