import tensorflow.compat.v1 as tf 
tf.disable_v2_behavior()
gf = tf.GraphDef()
pb_file_path=input("replace here your .pb file location ")
gf.ParseFromString(open(pb_file_path,'rb').read())
[n.name + '=>' +  n.op for n in gf.node if n.op in ( 'Softmax','Placeholder')]


import tensorflow as tf
converter = tf.compat.v1.lite.TFLiteConverter.from_frozen_graph(
    graph_def_file =pb_file_path,           #replace here your .pb file location 
    input_arrays = ['Placeholder'],
    output_arrays = ['final_result'] 
)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()
with tf.io.gfile.GFile('model.tflite', 'wb') as f:
  f.write(tflite_model)